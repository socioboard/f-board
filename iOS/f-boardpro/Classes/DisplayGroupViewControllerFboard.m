//
//  DisplayGroupViewControllerFboard.m
//  TwitterBoard
//
//  Created by GBS-ios on 4/23/15.
//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import "DisplayGroupViewControllerFboard.h"
#import <FacebookSDK/FacebookSDK.h>
#import "UIImageView+WebCache.h"
#import <FBSDKShareKit/FBSDKShareKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import "SUCacheFboard.h"
#import "SingletonClass.h"
#import "TableCustomCellFboard.h"

@interface DisplayGroupViewControllerFboard ()

@end

@implementation DisplayGroupViewControllerFboard

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [self createFollowTable];
    
//    [self fetchFeeds];

    
//    self.view.backgroundColor = [UIColor colorWithRed:26.0f/255.0f green:160.0f/255.0f blue:214.0f/255.0f alpha:1.0f];
    
    self.view.backgroundColor=[UIColor whiteColor];
    
//    self.msgArray = [[NSMutableArray alloc]init];
//    self.pictureArray = [[NSMutableArray alloc]init];
//    self.fromIdArray = [[NSMutableArray alloc]init];
//    self.fromNameArray=[[NSMutableArray alloc]init];
//    self.descArray=[[NSMutableArray alloc]init];
    
    
    self.msgArray = [[NSMutableArray alloc]init];
    self.pictureArray = [[NSMutableArray alloc]init];
    self.fromIdArray = [[NSMutableArray alloc]init];
    self.fromNameArray=[[NSMutableArray alloc]init];
    self.descArray=[[NSMutableArray alloc]init];
    self.dicArray=[[NSMutableArray alloc] init];
    self.feedDictArray=[[NSMutableArray alloc] init];

    
//    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
//    [[UINavigationBar appearance] setTintColor:[UIColor blackColor]];
//    
//    UIView *view= [[UIView alloc]init];
//     view.backgroundColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
//    view.frame = CGRectMake(0, 0, self.view.frame.size.width,40);
//    [self.view addSubview:view];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [button addTarget:self
               action:@selector(aMethod)
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitle:@"Back" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor blackColor] forState:
     UIControlStateNormal];
    button.frame = CGRectMake(0, 10.0, 160.0, 40.0);
    [self.view addSubview:button];
    
//    self.groupView=[[UIImageView alloc]init];
//    self.groupView.frame=CGRectMake(10, 80, 50, 50);
//    self.groupView.layer.cornerRadius=20;
//    [self.groupView setImageWithURL:[NSURL URLWithString:self.iconUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
//
//    [self.view addSubview:self.groupView];
   

//    self.descriptionLabel = [[UILabel alloc]init];
//    self.descriptionLabel.text=[NSString stringWithFormat:@"Description = %@",self.desc];
//    self.descriptionLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:12];
//    self.descriptionLabel.frame=CGRectMake(10, 140, 300, 30);
////    [self.view addSubview:self.descriptionLabel];
//    
//    self.emailLabel = [[UILabel alloc]init];
//    self.emailLabel.text=[NSString stringWithFormat:@"Email of group = %@",self.email];
//
//    self.emailLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:12];
//    self.emailLabel.frame=CGRectMake(10, 175, 350, 30);
////    [self.view addSubview:self.emailLabel];
//    
//    self.ownerNameLabel = [[UILabel alloc]init];
//    self.ownerNameLabel.text=[NSString stringWithFormat:@"Group owner = %@",self.ownerName];
//
//    self.ownerNameLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:12];
//    self.ownerNameLabel.frame=CGRectMake(10, 210, 300, 30);
//    [self.view addSubview:self.ownerNameLabel];
    
    // Do any additional setup after loading the view.
}
-(void)createFollowTable
{
    
    
    UIView *headerView = [[UIView alloc]init];
    headerView.frame=CGRectMake(0, 0, SCREEN_WIDTH, 150);
    headerView.backgroundColor=[UIColor blackColor];
    
    
    UIView *footerView = [[UIView alloc]init];
    footerView.frame=CGRectMake(0, 0, SCREEN_WIDTH, 80);
    
    groupFeedsView=[[UITableView alloc]init];
    groupFeedsView.frame=CGRectMake(10, 10, SCREEN_WIDTH-20, self.view.frame.size.height+10);
    groupFeedsView.dataSource=self;
    groupFeedsView.delegate=self;
    groupFeedsView.backgroundColor=[UIColor whiteColor];
    groupFeedsView.tableFooterView=footerView;
    groupFeedsView.tableHeaderView=headerView;
    [self.view addSubview:groupFeedsView];
    
    
    self.coverImageview =[[UIImageView alloc]init];
    self.coverImageview.frame=CGRectMake(0, 0, headerView.frame.size.width, headerView.frame.size.height);
    [self.coverImageview.layer setBorderColor: [[UIColor blackColor] CGColor]];
    
    [self.coverImageview.layer setBorderWidth:2];
    self.coverImageview.backgroundColor=[UIColor blackColor];
    
    
    [headerView addSubview:self.coverImageview];
    
    NSLog(@"image url %@",self.coverString);
    if(self.coverString)
    {
        
    }
    else
    {
        
    }
    

    
    
    
    
}



-(void)fetchFeeds{
    
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    

    
    NSString *str1 =self.idStr;

    NSIndexPath *indexPath=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexPath.row+indexPath.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@/feed",str1] parameters:nil];
        
        
        NSMutableArray * findAllLikes=[[NSMutableArray alloc]init];
        NSMutableArray * findAllComments=[[NSMutableArray alloc]init];
        NSMutableArray * findAllShares = [[NSMutableArray alloc]init];
        
        
        
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
            
            
            NSString *groupId;
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                NSString *str1 = [dic objectForKey:@"picture"];
                NSString *str2 = [dic objectForKey:@"message"];
                
                NSLog(@"likes...%@",[dic objectForKey:@"likes"]);
                if([dic objectForKey:@"likes"])
                {
                    [findAllLikes addObject:[dic objectForKey:@"likes"]];
                }
                else
                {
                    [findAllLikes addObject:[NSNumber numberWithInt:0]];
                }
                NSLog(@"comments...%@",[dic objectForKey:@"comments"]);
                if([dic objectForKey:@"comments"])
                {
                    [findAllComments addObject:[dic objectForKey:@"comments"]];
                }
                else
                {
                    [findAllComments addObject:[NSNumber numberWithInt:0]];
                }
                NSLog(@"shares...%@",[dic objectForKey:@"shares"]);
                if([[dic objectForKey:@"shares"] objectForKey:@"count"])
                {
                    [findAllShares addObject:[[dic objectForKey:@"shares"] objectForKey:@"count"]];
                }
                else
                {
                    [findAllShares addObject:[NSNumber numberWithInt:0]];
                }
                
                

               
                
                
                
                groupId= [[dic objectForKey:@"from"]objectForKey:@"id"];
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
                
                if (groupId) {
                    [self.fromIdArray addObject:groupId];
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
                
                self.coverString = [result objectForKey:@"id"];
            }
            
             [self getBannertUrl:groupId];
            
            
            
            allLikesInPage=[NSArray arrayWithArray:findAllLikes];
            allCommentInpage=[NSArray arrayWithArray:findAllComments];
            allSharesInpPage = [NSArray arrayWithArray:findAllShares];

            
            [groupFeedsView reloadData];
         }];
    }
}



-(void)getBannertUrl:(NSString *)groupId
{
    //Start an activity indicator here
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        
        //Call your function or whatever work that needs to be done
        //Code in this part is run on a background thread
        
        NSError * error=nil;
        NSURLResponse * urlResponse=nil;
        NSURL * url=[NSURL  URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@?fields=cover&access_token=%@",groupId,[SingletonClass sharedState].accessTokenString]];
        
        NSMutableURLRequest * getRequest=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
        [getRequest setHTTPMethod:@"GET"];
        [getRequest addValue:@"application/x-www-form-urlencoded; charset=utf-8" forHTTPHeaderField:@"Content-Type"];
        NSData * data=[NSURLConnection sendSynchronousRequest:getRequest returningResponse:&urlResponse error:&error];
        if (data==nil)
        {
        }
        id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
        if(!error)
        {
            
        }
        
        NSLog(@" response of feeds %@",response);
        
        bannerImageUrl=[NSURL URLWithString:[[response objectForKey:@"cover"] objectForKey:@"source"]];
        
        dispatch_async(dispatch_get_main_queue(), ^(void) {
            
            //Stop your activity indicator or anything else with the GUI
            //Code here is run on the main thread
            [self.coverImageview setImageWithURL:bannerImageUrl placeholderImage:[UIImage imageNamed:@""]];
            
        });
    });
}






-(void)viewWillAppear:(BOOL)animated

{
    
    [self fetchFeeds];
    
    
}


-(void)aMethod
{
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Display1";
    
   TableCustomCellFboard *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[TableCustomCellFboard alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    //    cell.backgroundColor= [UIColor redColor];
    
    cell.layer.cornerRadius=5;
    [cell.layer setBorderColor: [[UIColor blackColor] CGColor]];
    [cell.layer setBorderWidth:1];

    
    
//    NSString *str1 =[self.msgArray objectAtIndex:indexPath.section];
//    NSString *str2 = [self.descArray objectAtIndex:indexPath.section];
//
//   
//    if (![str1 isEqualToString:@"nil"]) {
//        cell.userNameDesc.text=str1;
//    }else{
//        cell.userNameDesc.text=@"nothing";
//    }
//    cell.likeCountLabel.text = @"";
//    cell.commentCountLabel.text=@"";
//    NSString *picUrl =[self.pictureArray objectAtIndex:indexPath.section];
//    
//    
//    if (![picUrl isEqualToString:@"nil"]) {
//        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
//    }else{
//        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
//    }
//    
//    NSString *fromIdImage = [self.fromIdArray objectAtIndex:indexPath.section];
//    
//    if (![fromIdImage isEqualToString:@"nil"]) {
//        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
//    }else{
//        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
//    }
//    
//    NSString *fromLabel1 = [self.fromNameArray objectAtIndex:indexPath.section];
//    cell.fromLabel.text = fromLabel1;
//    
//
    NSString *str1 =[self.msgArray objectAtIndex:indexPath.section];
    NSString *str2 = [self.descArray objectAtIndex:indexPath.section];
    if (![str1 isEqualToString:@"nil"]) {
        cell.userNameDesc.text=str1;
    }else{
        if (str2) {
            cell.userNameDesc.text=str2;
        }else{
            cell.userNameDesc.text=@"Posted something via Facebook";
        }
    }
    CGFloat heightOfDescriptionLbl=[self calculateHeight:cell.userNameDesc.text];
    cell.userNameDesc.frame=CGRectMake(75, 40, self.view.frame.size.width-100,heightOfDescriptionLbl+10);
    cell.likeCountLabel.text = @"";
    cell.commentCountLabel.text=@"";
    NSString *picUrl =[self.pictureArray objectAtIndex:indexPath.section];
    
    
    if (![picUrl isEqualToString:@"nil"]) {
        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *fromIdImage = [self.fromIdArray objectAtIndex:indexPath.section];
    
    if (![fromIdImage isEqualToString:@"nil"]) {
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *fromLabel1 = [self.fromNameArray objectAtIndex:indexPath.section ];
    cell.fromLabel.text = fromLabel1;

    
    
    
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    
    return 25;
    
}


-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    
    
    
    UIView *viewForFooter=[[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width,25)];
    viewForFooter.backgroundColor=[UIColor clearColor];
    
    
    for(int i=1;i<3;i++)
    {
        UIView *lineView=[[UIView alloc] initWithFrame:CGRectMake(100*i, 5, 0.5,25)];
        lineView.backgroundColor=[UIColor grayColor];
        [viewForFooter addSubview:lineView];
        
    }
    
    UIView *lineView1=[[UIView alloc] initWithFrame:CGRectMake(0, 5, SCREEN_WIDTH,0.5)];
    lineView1.backgroundColor=[UIColor grayColor];
    [viewForFooter addSubview:lineView1];
    
    UIView *lineView2=[[UIView alloc] initWithFrame:CGRectMake(0, 30, SCREEN_WIDTH,0.5)];
    lineView2.backgroundColor=[UIColor grayColor];
    [viewForFooter addSubview:lineView2];
    
    
    
    UIButton *likeButton =[[UIButton alloc]init];
    likeButton.frame=CGRectMake(0, 8, 50, 20);
    [likeButton setTitle:@"Like" forState:UIControlStateNormal];
    likeButton.titleLabel.font = [UIFont systemFontOfSize:12];
    [likeButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    [viewForFooter addSubview:likeButton];
    
    UILabel * likeDisplay=[[UILabel alloc]initWithFrame:CGRectMake(45, 8, 60, 20)];
    if([[allLikesInPage objectAtIndex:section] isKindOfClass:[NSDictionary class]] )
    {
        [likeButton setTitle:@"Likes" forState:UIControlStateNormal];
        likeDisplay.text=[NSString stringWithFormat:@"%lu",(unsigned long)[[[allLikesInPage objectAtIndex:section] objectForKey:@"data"] count]];
    }
    
    
    likeDisplay.font=[UIFont systemFontOfSize:12];
    [viewForFooter addSubview:likeDisplay];
    
    UIButton *commentButton =[[UIButton alloc]init];
    commentButton.frame=CGRectMake(90, 8, 100, 20);
    [commentButton setTitle:@"Comment" forState:UIControlStateNormal];
    commentButton.titleLabel.font = [UIFont systemFontOfSize:12];
    [commentButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    [viewForFooter addSubview:commentButton];
    
    
    UILabel * commentDisplay=[[UILabel alloc]initWithFrame:CGRectMake(175, 8, 60, 20)];
    if([[allCommentInpage objectAtIndex:section] isKindOfClass:[NSDictionary class]] )
    {
        
        [commentButton setTitle:@"Comments" forState:UIControlStateNormal];        commentDisplay.text=[NSString stringWithFormat:@"%lu",(unsigned long)[[[allCommentInpage objectAtIndex:section] objectForKey:@"data"] count]];
    }
    
    
    commentDisplay.font=[UIFont systemFontOfSize:12];
    [viewForFooter addSubview:commentDisplay];
    
    
    
    
    UIButton *shareButton =[[UIButton alloc]init];
    shareButton.frame=CGRectMake(180, 8, 100, 20);
    [shareButton setTitle:@"share" forState:UIControlStateNormal];
    shareButton.titleLabel.font = [UIFont systemFontOfSize:12];
    [shareButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    [viewForFooter addSubview:shareButton];
    
    UILabel * sharesDisplay=[[UILabel alloc]initWithFrame:CGRectMake(255, 8, 60, 20)];
    if(![[allSharesInpPage objectAtIndex:section] intValue]==0 )
    {
        
        [shareButton setTitle:@"shares" forState:UIControlStateNormal];       sharesDisplay.text=[NSString stringWithFormat:@"%@",[allSharesInpPage objectAtIndex:section]];
    }
    
    
    sharesDisplay.font=[UIFont systemFontOfSize:12];
    [viewForFooter addSubview:sharesDisplay];
    

    
    
    return viewForFooter;
    
}


-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    
    
    UIView *viewForHeader=[[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width,25)];
    viewForHeader.backgroundColor=[UIColor clearColor];
    
    
    return viewForHeader;
    
}



-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 10;
}


-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return self.pictureArray.count;

}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
        return 1;
   
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CGFloat heightOfSecriptionLabel;
    NSString *str1 =[self.msgArray objectAtIndex:indexPath.section];
    NSString *str2 = [self.descArray objectAtIndex:indexPath.section];
    if (![str1 isEqualToString:@"nil"])
    {
        heightOfSecriptionLabel=[self calculateHeight:str1];
    }
    else
    {
        if (str2)
        {
            heightOfSecriptionLabel=[self calculateHeight:str2];
            
        }
        else
        {
        }
    }
    
    //---------
    
    return heightOfSecriptionLabel+80;
}

-(CGFloat)calculateHeight:(NSString *)stringData
{
    UILabel * lblDescription=[[UILabel alloc]init];
    lblDescription.font=[UIFont systemFontOfSize:12];
    // Create a paragraph style with the desired line break mode
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    paragraphStyle.lineBreakMode = NSLineBreakByWordWrapping;
    
    // Create the attributes dictionary with the font and paragraph style
    NSDictionary *attributes = @{
                                 NSFontAttributeName:lblDescription.font,
                                 NSParagraphStyleAttributeName:paragraphStyle
                                 };
    
    // Call boundingRectWithSize:options:attributes:context for the string
    CGRect textRect = [stringData boundingRectWithSize:CGSizeMake(self.view.frame.size.width-100,6000)
                                               options:NSStringDrawingUsesLineFragmentOrigin
                                            attributes:attributes
                                               context:nil];
    
    float height = textRect.size.height;
    // NSLog(@"height of row %f",height);
    return height;
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
