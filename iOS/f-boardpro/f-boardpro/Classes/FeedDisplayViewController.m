//
//  FeedDisplayViewController.m

//

//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import "FeedDisplayViewController.h"
#import "SUCache.h"
#import "PAPPhotoDetailsViewController.h"
#import "SingletonClass.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKShareKit/FBSDKShareKit.h>

@interface FeedDisplayViewController ()

@end

@implementation FeedDisplayViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor colorWithRed:26.0f/255.0f green:160.0f/255.0f blue:214.0f/255.0f alpha:1.0f];
    
    self.msgArray = [[NSMutableArray alloc]init];
    self.pictureArray = [[NSMutableArray alloc]init];
    self.fromIdArray = [[NSMutableArray alloc]init];
    self.fromNameArray=[[NSMutableArray alloc]init];
    self.descArray=[[NSMutableArray alloc]init];
    
//    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
//    [[UINavigationBar appearance] setTintColor:[UIColor blackColor]];
    
    UIView *view= [[UIView alloc]init];
    view.backgroundColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
    view.frame = CGRectMake(0, 0, self.view.frame.size.width, 50);
    [self.view addSubview:view];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [button addTarget:self
               action:@selector(aMethod)
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitle:@"Back" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor blackColor] forState:
     UIControlStateNormal];
    button.frame = CGRectMake(0, 10.0, 160.0, 40.0);
    [self.view addSubview:button];
    
   
    
    [self createFollowTable];

    // Do any additional setup after loading the view.
}
-(void)createFollowTable
{
    feedTableView=[[UITableView alloc]initWithFrame:CGRectMake(0, 40, self.view.frame.size.width,self.view.frame.size.height-40) style:UITableViewStylePlain];
    feedTableView.dataSource=self;
    feedTableView.delegate=self;
    feedTableView.backgroundColor=[UIColor whiteColor];
    [self.view addSubview:feedTableView];
}
-(void)fetchFeeds{
    
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    

    
    NSIndexPath *path=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:path.row+path.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        NSString *str1 =self.idStr;

        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@/feed",str1] parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
            
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                NSString *str1 = [dic objectForKey:@"picture"];
                NSString *str2 = [dic objectForKey:@"message"];
                NSString *str3= [[dic objectForKey:@"from"]objectForKey:@"id"];
                NSString *str4= [[dic objectForKey:@"from"]objectForKey:@"name"];
                NSString *str5= [dic objectForKey:@"description"];
                
                if (str1) {
                    [self.pictureArray addObject:str1];
                }else{
                    [self.pictureArray addObject:@"nil"];
                }
                
                
                if (str2) {
                    [self.msgArray addObject:str2];
                }else{
                    [self.msgArray addObject:@"nil"];
                }
                
                if (str3) {
                    [self.fromIdArray addObject:str3];
                }else{
                    [self.fromIdArray addObject:@"nil"];
                }
                
                if (str4) {
                    [self.fromNameArray addObject:str4];
                }else{
                    [self.fromNameArray addObject:@"nil"];
                }
                
                if (str5) {
                    [self.descArray addObject:str5];
                }else{
                    [self.descArray addObject:@"nil"];
                }
            }
            [feedTableView reloadData];
        }];
        
        }
}

-(void)viewWillAppear:(BOOL)animated{

[self fetchFeeds];

}
-(void)aMethod{
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Display1";
    
    TableCustomCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[TableCustomCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    //    cell.backgroundColor= [UIColor redColor];
    
    
    NSString *str1 =[self.msgArray objectAtIndex:indexPath.row];
    NSString *str2 = [self.descArray objectAtIndex:indexPath.row];
    
    if (![str1 isEqualToString:@"nil"]) {
        cell.userNameDesc.text=str1;
    }else{
        if (str2) {
            cell.userNameDesc.text=str2;
        }else{
             cell.userNameDesc.text=@"Posted something via Facebook";
        }
    }
    
    cell.likeCountLabel.text = @"";
    cell.commentCountLabel.text=@"";
    NSString *picUrl =[self.pictureArray objectAtIndex:indexPath.row];
    
    
    if (![picUrl isEqualToString:@"nil"]) {
        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *fromIdImage = [self.fromIdArray objectAtIndex:indexPath.row];
    
    if (![fromIdImage isEqualToString:@"nil"]) {
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *fromLabel1 = [self.fromNameArray objectAtIndex:indexPath.row ];
    cell.fromLabel.text = fromLabel1;
    
    
//    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:cell.fromLabel.text];
//        // Add Background Color for Smooth rendering
//    [attributedString setAttributes:@{NSBackgroundColorAttributeName:[UIColor blackColor]} range:NSMakeRange(0, attributedString.length)];
//        // Add Main Font Color
//    [attributedString setAttributes:@{NSForegroundColorAttributeName:[UIColor colorWithWhite:0.23 alpha:1.0]} range:NSMakeRange(0, attributedString.length)];
//        // Add paragraph style
//    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
//    [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
//    [attributedString setAttributes:@{NSParagraphStyleAttributeName:paragraphStyle} range:NSMakeRange(0, attributedString.length)];
//        // Add Font
//    [attributedString setAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:17]} range:NSMakeRange(0, attributedString.length)];
//        // And finally set the text on the label to use this
//        //    label.attributedText = attributedString;
//    
//        // Phew. Now let's make the Bounding Rect
//    CGRect expectedLabelSize= [attributedString boundingRectWithSize:CGSizeMake(cell.fromLabel.frame.size.width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];
//    
//    cell.fromLabel.frame= expectedLabelSize;
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
}

-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return 1;
}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.pictureArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 250;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
