//
//  ScheduledPostController.m
//  f-boardpro
//
//  Created  on 5/5/15.
//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import "ScheduledPostController.h"
#import "TableCustomCell.h"
#import "UIImageView+WebCache.h"
#import <sqlite3.h>

@interface ScheduledPostController ()

@end

@implementation ScheduledPostController



- (void)viewDidLoad {
    [super viewDidLoad];
    
    if (IS_IPHONE_4_OR_LESS) {
        self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg_view.png"]];
        
    }else if(IS_IPHONE_5){
        self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg_view_iphone5.png"]];
        
        
    }else if(IS_IPHONE_6){
        self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg_view_iphone6@2x.png"]];
        
        
    }else if(IS_IPHONE_6P){
        self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg_view@3x.png"]];
    }

//    [self retriveAllSchedulePost];
    [self createFollowTable];
    // Do any additional setup after loading the view.
}


-(void)retriveAllSchedulePost{
    self.TextArray=[[NSMutableArray alloc] init];
    self.dataArray=[[NSMutableArray alloc] init];
    self.fbIDArray=[[NSMutableArray alloc] init];
    NSData* data = nil;
    NSString* sqliteQuery = [NSString stringWithFormat:@"SELECT * FROM IMAGES"];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSLog(@"-------%@",paths);
    sqlite3_stmt *stmt=nil;
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *databasePath = [documentsDirectory stringByAppendingPathComponent:@"ImageDataBase.sqlite"];
    
    sqlite3_stmt* statement;
    if(sqlite3_open([databasePath UTF8String], &_databaseHandle)==SQLITE_OK){
        if( sqlite3_prepare_v2(_databaseHandle, [sqliteQuery UTF8String], -1, &statement, NULL) == SQLITE_OK )
        {
            while( sqlite3_step(statement) == SQLITE_ROW )
            {
                
                NSString *strURl = [NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 0)];
                [self.fbIDArray addObject:strURl];
                int length = sqlite3_column_bytes(statement, 1);
                data       = [NSData dataWithBytes:sqlite3_column_blob(statement, 1) length:length];
                [self.dataArray addObject:data];
                self.accesstokenString = [NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 2)];
                NSString *strText=[NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 3)];
                [self.TextArray addObject:strText];
                
                NSLog(@"name string %@",self.accesstokenString);
                
            }
        }else{
            NSLog( @"SaveBody: Failed from sqlite3_prepare_v2. Error is:  %s", sqlite3_errmsg(_databaseHandle) );
        }
    }
        // Finalize and close database.
    sqlite3_finalize(statement);
    sqlite3_close(_databaseHandle);
    
    
    [groupTableView reloadData];


}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    NSString *strMsg=[self.TextArray objectAtIndex:indexPath.row];
    
    
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:strMsg];
        // Add Background Color for Smooth rendering
    [attributedString setAttributes:@{NSBackgroundColorAttributeName:[UIColor blackColor]} range:NSMakeRange(0, attributedString.length)];
        // Add Main Font Color
    [attributedString setAttributes:@{NSForegroundColorAttributeName:[UIColor colorWithWhite:0.23 alpha:1.0]} range:NSMakeRange(0, attributedString.length)];
        // Add paragraph style
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
    [attributedString setAttributes:@{NSParagraphStyleAttributeName:paragraphStyle} range:NSMakeRange(0, attributedString.length)];
        // Add Font
    [attributedString setAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:17]} range:NSMakeRange(0, attributedString.length)];
        // And finally set the text on the label to use this
//    label.attributedText = attributedString;
    
        // Phew. Now let's make the Bounding Rect
    CGRect boundingRect = [attributedString boundingRectWithSize:CGSizeMake(tableView.frame.size.width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];
    
        // Now let's set the label size from there
//    CGFloat frame = CGRectMake(label.frame.origin.x, label.frame.origin.y, boundingRect.size.width, boundingRect.size.height);

    return 50 +boundingRect.size.height;
}

-(void)createFollowTable
{
    groupTableView=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width,self.view.frame.size.height) style:UITableViewStylePlain];
    groupTableView.dataSource=self;
    groupTableView.delegate=self;
    groupTableView.backgroundColor=[UIColor whiteColor];
    [self.view addSubview:groupTableView];
}

-(void)viewWillAppear:(BOOL)animated{
    self.navigationController.navigationBarHidden=NO;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Follow";
    
    TableCustomCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[TableCustomCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    NSString *fromIdImage = [self.fbIDArray objectAtIndex:indexPath.row];
    
    if (![fromIdImage isEqualToString:@"nil"]) {
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:fromIdImage] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    cell.fromUserImage.frame=CGRectMake(10, 22, 40, 40);
    cell.userNameDesc.text=[self.TextArray objectAtIndex:indexPath.row];
    CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    CGSize expectedLabelSize = [cell.userNameDesc.text sizeWithFont:[UIFont fontWithName:@"ariel" size:5] constrainedToSize:maximumLabelSize lineBreakMode: NSLineBreakByWordWrapping];
           //adjust the label the the new height.
    CGRect newFrame = cell.userNameDesc.frame;
    newFrame.size.height = expectedLabelSize.height;
    newFrame.size.width = cell.userNameDesc.frame.size.width;
//    cell.userNameDesc.frame = newFrame;
    cell.userNameDesc.frame=CGRectMake(65, 12,tableView.frame.size.width-115, newFrame.size.height);
    [cell.userNameDesc sizeToFit];
    cell.likeCountLabel.text = @"";
    cell.commentCountLabel.text = @"";
    UIImage *Postedimage = [UIImage imageWithData:[self.dataArray objectAtIndex:indexPath.row]];
    cell.userImage.image=Postedimage;
    cell.userImage.frame=CGRectMake(tableView.frame.size.width-60, 22, 50, 50);
    
    return cell;
}

//-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
//    return 100;
//}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{

    return self.fbIDArray.count;
}

-(void)viewDidAppear:(BOOL)animated{
    [self retriveAllSchedulePost];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
