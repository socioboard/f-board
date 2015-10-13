//
//  FollwViewController.m
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import "MYFeedFboard.h"
#import "SUCacheFboard.h"
#import "AppDelegateFboard.h"
#import "MBProgressHUD.h"
#import "TableCustomCellFboard.h"
#import "SingletonClass.h"
#import "CommentsViewControllerFboard.h"
#import <FacebookSDK/FacebookSDK.h>
#import "UIImageView+WebCache.h"
#import "ProgressHUD.h"
@interface MYFeedFboard ()
{
    NSMutableArray * likedFeeds;
}
@end

@implementation MYFeedFboard
- (void)viewDidLoad
{
    [super viewDidLoad];
    likedFeeds=[[NSMutableArray alloc]init];
//    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(fetchFeeds:) name:@"CurrentUserChangedNotification" object:nil];

    // Do any additional setup after loading the view.
 //   self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg.png"]];
    
   // self.view.backgroundColor=[UIColor colorWithRed:(CGFloat)220/255 green:(CGFloat)220/255 blue:(CGFloat)220/255 alpha:1];
    self.view.backgroundColor=[UIColor whiteColor];
    
   }

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    self.navigationController.navigationBarHidden=YES;

     [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(fetchFeeds:) name:@"CurrentUserChangedNotification" object:nil];
  [self fetchFeeds:@"nil"];
}

-(void)viewDidDisappear:(BOOL)animated{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];

}

-(void)changeUser:(NSNotificationCenter *)defaultCenter{


}


-(void)createFollowTable
{
    
    if (!followTableView) {
        
        UIView *footerView =[[UIView alloc]init];
        footerView.frame=CGRectMake(0, 0, SCREEN_WIDTH, 80);
        followTableView=[[UITableView alloc]initWithFrame:CGRectMake(10, 10, self.view.frame.size.width-20,self.view.frame.size.height) style:UITableViewStylePlain];
        followTableView.dataSource=self;
        followTableView.delegate=self;
        followTableView.tableFooterView=footerView;
        followTableView.backgroundColor=[UIColor clearColor];
        [self.view addSubview:followTableView];
    }else{
        [followTableView reloadData];
    }
}
-(void)fetchFeeds:(NSString *)accessToken
{
     BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
   
    [[AppDelegateFboard sharedAppDelegate]showHUDLoadingView:@""];

    NSIndexPath  *userIndex=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:userIndex.row+userIndex.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        
        NSLog(@"%@",[FBSDKAccessToken currentAccessToken].tokenString);
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/feed" parameters:nil];
   
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error)
         {
            
            [[AppDelegateFboard sharedAppDelegate]hideHUDLoadingView];
            self.pagingString = [[result objectForKey:@"paging"]objectForKey:@"next"];
            NSString *pageStr = [[result objectForKey:@"paging"]objectForKey:@"previous"];
            if (self.pagingString) {
                    //NSLog(@"self.pagestr is %@",self.pagingString);
            }
             self.feedDataArray=[result objectForKey:@"data"];
              dispatch_async(dispatch_get_main_queue(), ^{
              [self setLikesByUserArray];
              [self createFollowTable];
              });
        }];
    }
}


-(void)fetchHomeFeeds:(NSString *)accessToken{
    
    /*NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    NSString *accessTokn = [[NSUserDefaults standardUserDefaults]objectForKey:@"accessToken"];
    
    NSLog(@"access Token %@",accessTokn);
    NSString *userId = [[NSUserDefaults standardUserDefaults]objectForKey:@"userId"];
    userId=@"1501093480148539";
    
   
    NSString * urlStr=[NSString stringWithFormat:@"/%@/feed?access_token=%@",userId,accessTokn];
    
    urlStr=[urlStr stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
    FBSDKGraphRequest *requester = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/feed" parameters:nil];
    
    [requester startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *errort) {
        
    }];
    NSURL * url=[NSURL URLWithString:urlStr];
    
    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    
    
    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
    
    
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    
    NSMutableDictionary * innerJson = [NSJSONSerialization
                                       JSONObjectWithData:data options:kNilOptions error:&error
                                       ];
    
    NSLog(@"response is=========================== %@",response);
    
     */
    
    
}

-(void)getLikesCount{
    
  /*  NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    NSString *accessTokn = [[NSUserDefaults standardUserDefaults]objectForKey:@"LaccessToken"];
    
    NSLog(@"access Token %@",accessTokn);
    NSString *userId = [[NSUserDefaults standardUserDefaults]objectForKey:@"userId"];
    userId=@"1501093480148539";
    
    
    NSString * urlStr=[NSString stringWithFormat:@"/%@/feed?access_token=%@",userId,accessTokn];
    
    urlStr=[urlStr stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
   // FBRequest *friendRequest = [FBRequest requestForGraphPath:@"me/?fields=likes"];
    
    FBRequest *like = [FBRequest requestForGraphPath:@"1461321887470777?fields=likes"];
    
    [like startWithCompletionHandler:^(FBRequestConnection *connection, id result, NSError *error5) {
        
      //   NSLog(@"result is is    %@",result);
        
    }];
    
    NSURL * url=[NSURL URLWithString:urlStr];
    
    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    
    
    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
    
    
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    
    
    NSLog(@"response is=========================== %@",response);*/

}

//-(void)sliderValueChanged:(id)slider {
//    slide = slider.on;
//    
//    [followTableView beginUpdates];
//    
//    if (slider.on) {
//        [followTableView insertSections:[NSIndexSet indexSetWithIndex:0] withRowAnimation:UITableViewRowAnimationTop];
//        // TODO: update data model by inserting new section
//    } else {
//        [followTableView deleteSections:[NSIndexSet indexSetWithIndex:0] withRowAnimation:UITableViewRowAnimationTop];
//        // TODO: update data model by removing approprite section
//    }
//    
//    [followTableView endUpdates];
//}
//
//




-(CGRect)calculateHeight:(NSString *)text widthOfLabel:(CGFloat)widthOfLabel
{
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:text];
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
    CGRect expectedLabelSize= [attributedString boundingRectWithSize:CGSizeMake(widthOfLabel, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];
    
    
    //    CGSize expectedLabelSize = [cell.userNameDesc.text sizeWithFont:[UIFont fontWithName:@"ariel" size:5] constrainedToSize:maximumLabelSize lineBreakMode: NSLineBreakByWordWrapping];
    
    //adjust the label the the new height.
    return expectedLabelSize;
}
-(NSString *)dateDiff:(NSString *)origDate {
    
    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    [df setFormatterBehavior:NSDateFormatterBehavior10_4];
    [df setDateFormat:@"yyyy'-'MM'-'dd'T'HH':'mm':'ssZZZZ"];
    NSDate *convertedDate = [df dateFromString:origDate];
    NSDate *todayDate = [NSDate date];
    double ti = [convertedDate timeIntervalSinceDate:todayDate];
    ti = ti * -1;
    if(ti < 1) {
        return @"never";
    } else 	if (ti < 60) {
        return @"less than a minute ago";
    } else if (ti < 3600) {
        int diff = round(ti / 60);
        return [NSString stringWithFormat:@"%d minutes ago", diff];
    } else if (ti < 86400) {
        int diff = round(ti / 60 / 60);
        return[NSString stringWithFormat:@"%d hours ago", diff];
    } else if (ti < 2629743) {
        int diff = round(ti / 60 / 60 / 24);
        return[NSString stringWithFormat:@"%d days ago", diff];
    }  else if (ti > 31535999) {
        int diff = round(ti / 31535999);
        return[NSString stringWithFormat:@"%d Years ago", diff];
    }  else if (ti > 2628000) {
        int diff = round(ti / 2628000);
        return[NSString stringWithFormat:@"%d months ago", diff];
    }
    else {
        return @"never";
    }
}
-(void)shareButton:(UIButton*)shareBtn
{
    //start

    NSLog(@"tag value of button %ld",(long)shareBtn.tag);
    NSDictionary * feedData=[self.feedDataArray objectAtIndex:(int)shareBtn.tag];
    NSString * linkToShare=[[[feedData objectForKey:@"actions"] lastObject]  objectForKey:@"link"];
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
    
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:index.section+index.row].token;
    
    if (token)
    {
        if (linkToShare)
        {
            NSDictionary * parameters=@{@"link":linkToShare};
            [FBSDKAccessToken setCurrentAccessToken:token];
            FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/me/feed"] parameters:parameters HTTPMethod:@"POST"];
            
            [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
                
                if (self.photoDetail) {
                    self.photoDetail=nil;
                }
                //            self.photoDetail=[[PAPPhotoDetailsViewController alloc] initWithDict:result];
                //            self.navigationController.navigationBarHidden=NO;
                //            [self.navigationController pushViewController:self.photoDetail animated:YES];
                
            }];
            
 
        }
           }
 
}

-(void)commentButton:(UIButton *)commentsBtn{
    
    NSDictionary * dataDict=[self.feedDataArray objectAtIndex:(int)commentsBtn.tag];

    //start
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
    
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:index.section+index.row].token;
    
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",[dataDict objectForKey:@""]] parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
            
            if (self.photoDetail) {
                self.photoDetail=nil;
            }
            self.photoDetail=[[CommentsViewControllerFboard  alloc] initWithDict:result];
            self.navigationController.navigationBarHidden=NO;
            [self.navigationController pushViewController:self.photoDetail animated:YES];
            
        }];
        
    }
    

    
    //End
    
    
    
    
//  //  button.image=[[UIImage imageNamed:@"comment.png"]imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
//    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//    
//    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
//    
//    if (token) {
//        
//        [FBSDKAccessToken setCurrentAccessToken:token];
//        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",[self.grpIdArray objectAtIndex:button.tag]] parameters:nil];
//        
//        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
//            
//            
//            PAPPhotoDetailsViewController *photoDetail=[[PAPPhotoDetailsViewController alloc] initWithDict:result];
//            self.navigationController.navigationBarHidden=NO;
//            [self.navigationController pushViewController:photoDetail animated:YES];
//            
//            
//        }];
//        
//    }
//    
}

-(void)likeButton:(UIButton *)likeBtn
{
    
        NSDictionary * dataDict=[self.feedDataArray objectAtIndex:(int)likeBtn.tag];
        NSString * userId=[dataDict objectForKey:@"id"];
        NSString * likeRequest=[NSString stringWithFormat:@"/%@/likes",userId];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc]
                                      initWithGraphPath:likeRequest
                                      parameters:nil
                                      HTTPMethod:@"GET"];
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection,
                                              id result,
                                              NSError *error)
         {
            // Handle the result
             if(error)
             {
                 return;
             }
             BOOL found=NO;
             NSMutableArray * tempLikeResults=[[NSMutableArray alloc]initWithArray:[result objectForKey:@"data"]];
             for (int i=0; i<tempLikeResults.count; i++)
             {
                 NSDictionary * tempDict=[tempLikeResults objectAtIndex:i];
                 if([[tempDict objectForKey:@"id"] isEqualToString:[SingletonClass sharedState].fbUserId])
                 {
                     found=YES;
                     break;
                 }
             }
             if(!found)
             {
                 //Call More Likes Api
                 if([[result objectForKey:@"paging"]objectForKey:@"next"])
                 {
                     NSString * nextUrl=[[result objectForKey:@"paging"]objectForKey:@"next"];
                     [self getMoreLikes:nextUrl idOfFeed:userId];
                 }
                 else
                 {
                     //Call Like Api
                     [self likeAction:userId];
                     return;
                 }
             }
             else
             {
                 //Call the Unlike Api
                 
                 [self unlikeAction:userId];
                 return ;
             }
        }];
   // });

    /*NSString *idValue= [self.grpIdArray objectAtIndex:likeBtn.tag];
    int likeUnlikeStatus=[[likedFeeds objectAtIndex:(int)likeBtn.tag] intValue];
    if(likeUnlikeStatus==1)
    {
       
       [self unlikeAction:idValue button:likeBtn];
    }
    else
    {
        [self likeAction:idValue button:likeBtn];

    }
    */
    
}
-(void)getMoreLikes:(NSString*)nextUrl idOfFeed:(NSString*)feedId
{
    BOOL found=NO;
    NSError * error=nil;
    NSURLResponse * urlResponse=nil;
    NSURL * url=[NSURL  URLWithString:nextUrl];
    
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
    else
    {
        return;
    }
    
    NSLog(@" response of feeds %@",response);
    NSArray * tempArray=[response objectForKey:@"data"];

//Check if next is available or not
    if([[response objectForKey:@"paging"]objectForKey:@"next"])
    {
        for (int i=0; i<tempArray.count; i++)
        {
            NSDictionary * tempDict=[tempArray objectAtIndex:i];
            if([[tempDict objectForKey:@"id"] isEqualToString:[SingletonClass sharedState].fbUserId])
            {
                found=YES;
                break;
            }
        }
        if(!found)
        {
            //Call More Likes Api
            if([[response objectForKey:@"paging"]objectForKey:@"next"])
            {
                NSString * nextUrl=[[response objectForKey:@"paging"]objectForKey:@"next"];
                [self getMoreLikes:nextUrl idOfFeed:feedId];
                
            }
        }
        else
        {
            //Call the Unlike Api
            [self unlikeAction:feedId];
            return;
            
        }
    }
    else
    {
        //Call Like Api
        [self likeAction:feedId];
        return;
    }
    
}
#pragma mark Like and Unlike Actions
-(void)likeAction:(NSString *)idValue
{
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
    //    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:index.section+index.row].token;
    [FBSDKAccessToken setCurrentAccessToken:token];
    
    FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"%@/likes",idValue] parameters:nil HTTPMethod:@"POST"];
    
    [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id response, NSError *error) {
        NSString *str=[response valueForKey:@"success"];
        NSLog(@"%@",str);
        
        
        if ([response valueForKey:@"success"])
        {
            [ProgressHUD showSuccess:@"like"];
 
        }
        
        
    }];
    
}
-(void)unlikeAction:(NSString*)idValue
{
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
    //    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:index.section+index.row].token;
    [FBSDKAccessToken setCurrentAccessToken:token];
    
    FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"%@/likes",idValue] parameters:nil HTTPMethod:@"DELETE"];
    
    [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id response, NSError *error) {
        NSString *str=[response valueForKey:@"success"];
        [ProgressHUD showError:@"Unlike"];
        NSLog(@"%@",str);
    }];
    
}
#pragma mark Table Delegates
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Myfeed";
    
    TableCustomCellFboard *cell = (TableCustomCellFboard*)[tableView dequeueReusableCellWithIdentifier:nil];
    
    
    if (cell == nil)
    {
        cell = [[TableCustomCellFboard alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        // cell.likeCountLabel.text=@"10 Like";
        
        
    }
    
    NSDictionary * feedDataDict=[self.feedDataArray objectAtIndex:indexPath.section];
    
    //       [followTableView reloadSections:[NSIndexSet indexSetWithIndex:2] withRowAnimation:UITableViewRowAnimationTop];
    //
    //    [UIView transitionWithView:followTableView
    //                      duration:0.35f
    //                       options:UITableViewRowAnimationFade
    //                    animations:^(void)
    //     {
    //         [followTableView reloadData];
    //     }
    //                    completion:nil];
    
    
    [cell.layer setBorderColor: [[UIColor blackColor] CGColor]];
    [cell.layer setBorderWidth:1];
    
    cell.layer.cornerRadius=5;
    cell.timeLabel.text=@"";
    //    cell.backgroundColor= [UIColor redColor];
    NSString *ownerId=[[feedDataDict objectForKey:@"from"] objectForKey:@"id"];
    NSString *str =[feedDataDict objectForKey:@"picture"];
    NSString *timeStr=[feedDataDict objectForKey:@"created_time"];
    
    timeStr=[self dateDiff:timeStr];
    NSString *strurl=[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",ownerId];
    NSLog(@"Owner strUrl  %@",strurl);
    cell.userNameLabel.text=[[feedDataDict objectForKey:@"from"] objectForKey:@"name"];
    cell.fromUserImage.layer.cornerRadius=cell.fromUserImage.frame.size.width/2;
    [cell.fromUserImage setImageWithURL:[NSURL URLWithString:strurl]];
    cell.fromUserImage.clipsToBounds=YES;
    cell.timeLabel.text=timeStr;
    if (![str isEqualToString:@"nil"]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [cell.userImage setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        });
    }else{
        // cell.userImage.hidden=YES;
    }
    
    NSString *str1 =[feedDataDict objectForKey:@"message"];
    NSString *str2= [feedDataDict objectForKey:@"description"];
    if (str1) {
        cell.userNameDesc.text=str1;
    }else
    {
        if (str2)
        {
            cell.userNameDesc.text=str2;
        }else{
            cell.userNameDesc.text=[feedDataDict objectForKey:@"story"];
            //                   cell.userNameDesc.hidden=YES;
        }
        
    }
    CGRect expectedLabelSize;
    CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    if([cell.userNameDesc.text isEqualToString:@""]||!cell.userNameDesc.text)
    {
        expectedLabelSize=[self calculateHeight:@"" widthOfLabel:SCREEN_WIDTH-40];
    }
    else
    {
        expectedLabelSize=[self calculateHeight:cell.userNameDesc.text widthOfLabel:SCREEN_WIDTH-40];
    }
    
    if (cell.userNameDesc.text)
    {
        
        CGRect newFrame = cell.userNameDesc.frame;
        newFrame.size.height = expectedLabelSize.size.height;
        newFrame.size.width =  SCREEN_WIDTH-40;
        cell.userNameDesc.frame = newFrame;
        [cell.userNameDesc sizeToFit];
        CGRect rect=cell.userImage.frame;
        cell.userImage.frame=CGRectMake(rect.origin.x,cell.userNameDesc.frame.origin.y+cell.userNameDesc.frame.size.height+5, rect.size.width, rect.size.height);
        
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{

    
}
-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return self.feedDataArray.count;
}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    
    
        return 35;

}

//-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
//    
//    
//    
//    
//    UIView *viewForFooter=[[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width,25)];
//    viewForFooter.backgroundColor=[UIColor clearColor];
//    
//    
//    
//    for(int i=1;i<3;i++)
//    {
//        UIView *lineView=[[UIView alloc] initWithFrame:CGRectMake(100*i, 5, 0.5,25)];
//        lineView.backgroundColor=[UIColor grayColor];
//        [viewForFooter addSubview:lineView];
//        
//    }
//    
//
//    
//    UIView *lineView1=[[UIView alloc] initWithFrame:CGRectMake(0, 5, SCREEN_WIDTH,0.5)];
//    lineView1.backgroundColor=[UIColor grayColor];
//    [viewForFooter addSubview:lineView1];
//    
//    UIView *lineView2=[[UIView alloc] initWithFrame:CGRectMake(0, 30, SCREEN_WIDTH,0.5)];
//    lineView2.backgroundColor=[UIColor grayColor];
//    [viewForFooter addSubview:lineView2];
//    
//    
//    
//    UIButton *likeButton =[[UIButton alloc]init];
//    likeButton.frame=CGRectMake(0, 8, 50, 20);
//    [likeButton setTitle:@"Like" forState:UIControlStateNormal];
//    likeButton.titleLabel.font = [UIFont systemFontOfSize:12];
//    [likeButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//    [viewForFooter addSubview:likeButton];
//    
//    
//    
//    
//    UIButton *commentButton =[[UIButton alloc]init];
//    commentButton.frame=CGRectMake(100, 8, 100, 20);
//    [commentButton setTitle:@"Comment" forState:UIControlStateNormal];
//    commentButton.titleLabel.font = [UIFont systemFontOfSize:12];
//    [commentButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//    
//    [viewForFooter addSubview:commentButton];
//    
//    
//    
//    
//    UIButton *shareButton =[[UIButton alloc]init];
//    shareButton.frame=CGRectMake(200, 8, 100, 20);
//    [shareButton setTitle:@"share" forState:UIControlStateNormal];
//    shareButton.titleLabel.font = [UIFont systemFontOfSize:12];
//    [shareButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//    
//    [viewForFooter addSubview:shareButton];
//
//    
//    return viewForFooter;
//}

//start

-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    
    UIView *view=[[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width,0)];
    view.backgroundColor=[UIColor clearColor];
    NSDictionary * dataDict=[self.feedDataArray objectAtIndex:section];
    
    NSArray *likeArr=[[dataDict objectForKey:@"likes"] objectForKey:@"data"];
    NSArray *commentArr=[[dataDict  objectForKey:@"comments"] objectForKey:@"data"];
    NSString *likeTitle=[NSString stringWithFormat:@"Likes %lu",(unsigned long)likeArr.count];
    NSString *shareCount=[[dataDict objectForKey:@"shares"] objectForKey:@"count"];
    NSString *commentTitle=[NSString stringWithFormat:@"Comments %lu",(unsigned long)commentArr.count];
    
    
    UIButton *likeBtn=[[UIButton alloc]initWithFrame:CGRectMake(2,8,100,20)];
    likeBtn.tag=section;
    [likeBtn setTitle:likeTitle forState:UIControlStateNormal];
    [likeBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    likeBtn.titleLabel.textAlignment=NSTextAlignmentLeft;
    likeBtn.titleLabel.font=[UIFont systemFontOfSize:12];
    [ likeBtn addTarget:self action:@selector(likeButton:) forControlEvents:UIControlEventTouchUpInside];
    [view addSubview:likeBtn];
    //-----
//    if([[likedFeeds objectAtIndex:section]isEqualToNumber:@1])
//    {
//        [likeBtn setTitleColor:ThemeColorFboard forState:UIControlStateNormal];
//
//    }
//    else
//    {
//        [likeBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//
//   }
//--------
    UIView *likeLineView=[[UIView alloc] initWithFrame:CGRectMake(100, 5, 0.5,25)];
    likeLineView.backgroundColor=[UIColor grayColor];
    [view addSubview:likeLineView];
    //-------
    UIButton * commentsBtn=[[UIButton alloc]initWithFrame:CGRectMake(SCREEN_WIDTH/2-50,8,100,20)];
    commentsBtn.tag=section;
    commentsBtn.titleLabel.textAlignment=NSTextAlignmentCenter;
    [commentsBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [commentsBtn setTitle:commentTitle forState:UIControlStateNormal];
    commentsBtn.titleLabel.font=[UIFont systemFontOfSize:12];
     [ commentsBtn addTarget:self action:@selector(commentButton:) forControlEvents:UIControlEventTouchUpInside];
    [view addSubview:commentsBtn];
    //-----
    UIView *commentLineView=[[UIView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH/2+50, 5, 0.5,25)];
    commentLineView.backgroundColor=[UIColor grayColor];
    [view addSubview:commentLineView];
    //-------
    UIButton * shareBtn=[[UIButton alloc]initWithFrame:CGRectMake(SCREEN_WIDTH-120,8,100,20)];
    shareBtn.tag=section;
    shareBtn.titleLabel.textAlignment=NSTextAlignmentRight;
    [shareBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    shareBtn.titleLabel.font=[UIFont systemFontOfSize:12];
    [shareBtn setTitle:[NSString stringWithFormat:@"Shares %@",shareCount] forState:UIControlStateNormal];
    [shareBtn addTarget:self action:@selector(shareButton:) forControlEvents:UIControlEventTouchUpInside];
    [view addSubview:shareBtn];
    
    if(!shareCount)
    {
        [shareBtn setTitle:@"Share" forState:UIControlStateNormal];
        
        shareCount=@"";
    }
    if(!likeArr)
    {
        [likeBtn setTitle:@"Like" forState:UIControlStateNormal];
        
        
    }
    if(!commentArr)
    {
        [commentsBtn setTitle:@"Comment" forState:UIControlStateNormal];
    }
    

    

    /*toolBar1 = [[UIToolbar alloc] initWithFrame:CGRectMake(0,10,tableView.frame.size.width , 20)];
   // [toolBar1 setBarStyle:UIBarStyleDefault];
    toolBar1.backgroundColor=[UIColor redColor];
   // [toolBar1 setBarStyle:UIBarStyleDefault];
    
    //toolBar1.tintColor=[UIColor blueColor];
    
    
        NSMutableArray *barItems = [[NSMutableArray alloc] init];
    
    UIBarButtonItem *likeButton = [[UIBarButtonItem alloc]initWithTitle:likeTitle style:UIBarButtonItemStyleBordered target:self action:@selector(likeButton:)];
    likeButton.tag=section;
    [barItems addObject:likeButton];
    
    
    UIBarButtonItem *flexibleSpace1 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:@selector(commentButton:)];
    [flexibleSpace1 setTitle:@"Comments"];
    
    [barItems addObject:flexibleSpace1];
    
    UIBarButtonItem *commentButton = [[UIBarButtonItem alloc]initWithTitle:commentTitle style:UIBarButtonItemStyleBordered target:self action:@selector(commentButton:)];
 
    commentButton.tag=section;
    [barItems addObject:commentButton];
    
    UIBarButtonItem *flexibleSpace2 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:@selector(commentButton:)];
    [flexibleSpace2 setTitle:@"Comments"];
    
    [barItems addObject:flexibleSpace2];
    
    if (shareCount) {
        UIBarButtonItem *shareButton = [[UIBarButtonItem alloc]initWithTitle:[NSString stringWithFormat:@"Shares %@",shareCount]  style:UIBarButtonItemStyleBordered target:self action:@selector(commentButton:)];
        
        shareButton.tag=section;
        [barItems addObject:shareButton];
    }else{
    
        UIBarButtonItem *shareButton = [[UIBarButtonItem alloc]initWithTitle:@"Share"   style:UIBarButtonItemStyleBordered target:self action:@selector(commentButton:)];
        
        shareButton.tag=section;
        [barItems addObject:shareButton];
    
    
    }
   


    
   [toolBar1 setItems:barItems animated:YES];*/
    
    
   
    
  
    
    //[view addSubview:toolBar1];
    
    return view;

}
-(void)setLikesByUserArray
{
 
//   for (int i=0; i<self.feedDictArray.count; i++)
//    {
//         NSArray *likeArr=[[[self.feedDictArray objectAtIndex:i] objectForKey:@"likes"] objectForKey:@"data"];
//        BOOL isLiked=false;
//        for (int j=0; j<likeArr.count; j++)
//        {
//            NSDictionary * likeDict=[likeArr objectAtIndex:j];
//            if([[likeDict objectForKey:@"id"]isEqualToString:[SingletonClass sharedState].fbUserId])
//            {
//                [likedFeeds addObject:[NSNumber numberWithInt:1]];
//                isLiked=YES;
//                break;
//            }
//           
//
//        }
//        
//        if(!isLiked)
//        {
//            [likedFeeds addObject:[NSNumber numberWithInt:0]];
//  
//        }
//    }
}
 // end

-(void)didTapLikeComment:(UIBarButtonItem *)bar{


}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary * feedDataDict=[self.feedDataArray objectAtIndex:indexPath.section];

    NSString *str1 =[feedDataDict objectForKey:@"message"];
    NSString *str2= [feedDataDict objectForKey:@"description"];
     NSString *str =[feedDataDict objectForKey:@"picture"];
     NSString *str3   = [feedDataDict objectForKey:@"story"];

    NSString *msgStr;
    if (str1) {
        msgStr=str1;
    }else
    {
    if (str2)
    {

        msgStr=str2;
    }else{
        msgStr=str3;    
    }
    }
    
    if (msgStr)
    {
        
        CGRect boundingRect = [self calculateHeight:msgStr widthOfLabel:SCREEN_WIDTH-60];
    
    
    if (str)
    {
        return boundingRect.size.height+110+40;
    }
    else
    {
        
      return boundingRect.size.height+65;
    }
    }
    else
    {
    
        if (str)
        {
            return 110+75;
        }
        else
        {
            return 65;
        }
    }
   
}

-(void) tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
//    NSArray *likeArr=[[[self.feedDictArray objectAtIndex:indexPath.section] objectForKey:@"likes"] objectForKey:@"data"];
//    NSArray *commentArr=[[[self.feedDictArray objectAtIndex:indexPath.section] objectForKey:@"comments"] objectForKey:@"data"];
//    NSString *likeTitle=[NSString stringWithFormat:@"Likes %lu",(unsigned long)likeArr.count];
//    
//    NSString *commentTitle=[NSString stringWithFormat:@"Comments %lu",(unsigned long)commentArr.count];
    
    NSLog(@"index is %ld",(long)indexPath.section);
    
    if (indexPath.section == self.feedDataArray.count-1) {
       [self retrieveMoreObjects];
    }
    if (cell!=nil) {
       
    }
    
}

-(void) retrieveMoreObjects
{
    [NSThread detachNewThreadSelector:@selector(addProgressHud) toTarget:self withObject:nil];
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    
    NSString *accessToken1=[[NSUserDefaults standardUserDefaults]objectForKey:@"accessToken"];
    
    NSIndexPath  *userIndex=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:userIndex.row+userIndex.section].token;
    if (token) {
        [FBSDKAccessToken setCurrentAccessToken:token];

//    FBSDKAccessToken *token = [SUCache itemForSlot:slot].token;
    
    NSString *str1 =self.pagingString;
    
    NSString * urlStr=str1;
    
    NSString * urlStr2=[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSURL *url=[NSURL URLWithString:urlStr2];
    
    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    
    
    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
    
    
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    NSLog(@"reponse is %@",response);
    
    self.pagingString = [[response objectForKey:@"paging"]objectForKey:@"next"];
    NSString *pageStr = [[response objectForKey:@"paging"]objectForKey:@"previous"];
        NSMutableArray * tempArray=[[NSMutableArray alloc]initWithArray:self.feedDataArray];
        [tempArray addObjectsFromArray:[response objectForKey:@"data"]];
        self.feedDataArray=[NSArray arrayWithArray:tempArray];
    if (self.pagingString) {
            // NSLog(@"self.pagestr is %@",self.pagingString);
    }
    
            dispatch_async(dispatch_get_main_queue(), ^{
                     //Reloading table
                [ProgressHUD dismiss];
                [self setLikesByUserArray];
                [followTableView reloadData];
            });
        
    }
}
-(void)addProgressHud
{
    dispatch_async(dispatch_get_main_queue(), ^(void){
        
        [ProgressHUD show:@"Loading..."];
    });
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
